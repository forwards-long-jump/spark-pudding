function getRequiredComponents()
	return {"test"}
end

function isPausable()
	return false
end

function update(entity)
  entity.test.x = entity.test.x + 1
end